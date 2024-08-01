package tech.masagal.markusai.chat.ai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;
import tech.masagal.markusai.chat.ChatMessage;
import tech.masagal.markusai.inventory.model.InventoryItem;
import tech.masagal.markusai.inventory.service.InventoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SystemMessageConfiguration {
    Logger logger = LogManager.getLogger();

    private final List<String> insertStatements = ReadTemplate.readTemplate();
    private final InventoryService inventoryService;

    public SystemMessageConfiguration(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Bean(name = "ChatGPT Instructions")
    public ChatMessage systemMessage() {
        return new ChatMessage("" + insertStatements, ChatMessage.Role.SYSTEM);
    }

    @Bean
    @Scope("prototype")
    @Primary
    public ChatMessage pojoChatGptInstructions() {
        List<InventoryItem> inventoryItems = inventoryService.getAll();
        logger.info("the number of distinct inventory items is: {}", inventoryItems.size());
        String msg = """
                You assist employees in making requests for office supplies.
                The following office supplies are available:
                """;
        msg += inventoryItems.stream().map((item) -> {
            logger.info("here's some stuff: {}", item.getLabel())   ;
            return "Product id: " + item.getProduct().getId()
                            + " Name: " + item.getLabel()
                            + " Quantity in stock: " + item.getQuantity().toString()
                            + " Location: "+item.getLocation();
                })
                .collect(Collectors.joining("\n"));
        msg += """
                If the user is asking for something that is already available in the inventory (with a non-zero quantity), 
                please return a message informing the user that the item is already available.
                If the user is asking for something that is in the inventory, but with a zero quantity,
                please prepare a Request object in the following JSON.
                If the user is asking for something that does not exist in the inventory,
                please inform them that they are not authorized to request that item.
                You are allowed to inform them about the other products that are in the inventory.
                You are also allowed to suggest a product similar to what they are asking for. 
                The user may report that inventory quantities differ. In this case, update the inventory quantity.
                If the user says we are out of something, both update the inventory and make a request.
                If the user decides to take an item, update the inventory, but do not make a request.
                The quantity must not be negative.
                Do not send the request multiple times.
                Be a little skeptical, do not always trust the user immediately, but do not persist in refusing.
                Your response should always be a JSON object in the following format:
                     {
                     "messageToUser": <submit text to be forwarded to the user>,
                     "request": <if a request should be made, on this format:>
                            {"products": [{product_id: <product id>, quantity: <quantity>}]},
                     "inventoryUpdateRequest": <if the user states a different quantity in inventory>
                            { "product_id": <product_id>, newQuantity: <new quantity> }
                     }
                """;
        logger.info("System message reads: {}", msg);
        return new ChatMessage(msg, ChatMessage.Role.SYSTEM);
    }
}
