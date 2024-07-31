package tech.masagal.markusai.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.masagal.markusai.chat.ai.ChatResult;
import tech.masagal.markusai.chat.ai.PojoChatGptManager;
import tech.masagal.markusai.chat.sql.SqlHandler;
import tech.masagal.markusai.inventory.model.InventoryItem;
import tech.masagal.markusai.inventory.service.InventoryService;
import tech.masagal.markusai.products.Product;
import tech.masagal.markusai.products.ProductDbRepo;
import tech.masagal.markusai.request.Request;
import tech.masagal.markusai.request.RequestProduct;
import tech.masagal.markusai.request.RequestRepository;
import tech.masagal.markusai.user.User;
import tech.masagal.markusai.user.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Primary
public class PojoChatService extends ChatService {
    Logger logger = LogManager.getLogger();

    private final InventoryService inventoryService;
    private final ProductDbRepo productDbRepo;
    private final UserRepository userRepo;
    private final RequestRepository requestRepo;

    public PojoChatService(PojoChatGptManager aiManager, SqlHandler sqlHandler, InventoryService inventoryService, ProductDbRepo productDbRepo, UserRepository userRepo, RequestRepository requestRepo ) {
        super(aiManager, sqlHandler);
        this.inventoryService = inventoryService;
        this.productDbRepo = productDbRepo;
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
    }

    @Override
    public ChatMessage respondToUserMessage(ChatMessage userMessage) throws Exception {
        conversationHistory.add(userMessage);
        ChatResult result = this.aiManager.getChatCompletion(this.conversationHistory);
        if(result.request().isPresent()) {
            processRequest(result.request().get());
        }
        if(result.inventoryUpdateRequest().isPresent()) {
            processInventoryUpdate(result.inventoryUpdateRequest().get());
        }
        ChatMessage output = new ChatMessage(result.chatMessage(), ChatMessage.Role.ASSISTANT);
        conversationHistory.add(output);
        return output;
    }

    private void processInventoryUpdate(ChatResult.InventoryUpdateRequest request) {
        InventoryItem item = inventoryService.getAll().stream().filter((i) ->
                i.getProduct()
                        .getId()
                        .equals(Long.valueOf(request.productId())))
                .findFirst().orElseThrow(NoSuchElementException::new);
        inventoryService.updateQuantity(item, request.newQuantity());
    }

    private void processRequest(ChatResult.ChatResultRequest request) {
        User user = userRepo.getReferenceById(1L);
        List<RequestProduct> requestedProducts = request.products().stream()
                        .map((product) -> {
                            Product p = productDbRepo.findById(Long.valueOf((product.productId()))).orElseThrow(NoSuchElementException::new);
                            logger.info("found product for this id: {}", p.getId());
                            return new RequestProduct(p, product.quantity());
                        }).toList();
        logger.info("product list has length {}", requestedProducts.size());
        Request req = new Request(user);
        requestedProducts.forEach((rp) -> {
            rp.setRequest(req);
        });
        req.setProducts(requestedProducts);
        logger.info("request has product list size {}", req.getProducts().size());

        requestRepo.save(req);
        logger.info("just a line to look at");
    }
}
