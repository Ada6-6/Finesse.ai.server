package com.powerpuff.billServer.controller;

import com.powerpuff.billServer.model.Transaction;
import com.powerpuff.billServer.service.TransactionService;
import com.powerpuff.billServer.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private OpenAIService openAIService;

    // 手动添加账单
    @PostMapping("/save")
    public String add(@RequestBody Transaction transaction){
        transactionService.saveTransaction(transaction);
        return "New transaction is added";
    }

    // AI 帮助生成账单
    @PostMapping("/addWithAI")
    public ResponseEntity<String> addWithAI( @Parameter(description = "User's message for AI processing",
            schema = @Schema(defaultValue = "Bought groceries at supersotre for $72.50 on 08/15/2024"))
                                                 @RequestBody String userMessage) {
        try {
            if (userMessage == null || userMessage.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message cannot be empty");
            }

            // Call the AI service and get the response
            JSONObject openAIResponse = openAIService.callOpenAI(userMessage);

            // Use AI service to generate and save the Transaction object
            Transaction transaction = openAIService.parseAndSaveTransaction(openAIResponse);

            // Save the transaction to the database
            transactionService.saveTransaction(transaction);

            // Return the formatted JSON response with indentation
            return ResponseEntity.ok(openAIResponse.toString(4));  // 4 spaces for indentation
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing transaction: " + e.getMessage());
        }
    }


    // Upload an image
    @PostMapping(value = "/addWithAIImg", consumes = "multipart/form-data")
    @Operation(summary = "Upload a bill image", description = "Upload a bill image to the server.")
    public ResponseEntity<String> addWithAIImg(
            @Parameter(description = "The image file to upload", required = true)
            @RequestParam("image") MultipartFile image) {
        try {
            // Call the AI service to process the image
            JSONObject aiResponse = openAIService.processImage(image);

            // TODO Check if the AI response indicates it's a valid transaction
//            if (!aiResponse.has("transaction") || aiResponse.getString("transaction").isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The uploaded image is not a valid transaction.");
//            }

            // Parse the AI response and create a transaction
            Transaction transaction = openAIService.parseAndSaveTransaction(aiResponse);

            // Store the receipt image path in the transaction
            //TODO setReceiptImage
//            String imagePath = openAIService.convertFileToBase64(image);
//            transaction.setReceiptImage(imagePath);

            // Save the transaction to the database
            transactionService.saveTransaction(transaction);

            // Return the formatted JSON response with indentation
            return ResponseEntity.ok(aiResponse.toString(4));  // 4 spaces for indentation
//            return ResponseEntity.ok("Bill processed and transaction saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the bill: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
//    public List<Transaction> getAllTransaction(
//            @RequestParam(value = "sortOrder", required = false) String sortOrder){
//        return transactionService.getAllTransaction(sortOrder);
//    }



//    /transactions?sortOrder=asc&category=Groceries&transactionType=outcome
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @Parameter(description = "Sort order (asc or desc). If not provided, defaulting to descending order.")
            @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Transaction category filter (SHOPPING, HOUSING...). If not provided, all categories are listed.")
            @RequestParam(required = false) String category,
            @Parameter(description = "Transaction type filter (income or outcome). If not provided, both types are listed.", example = " ")
            @RequestParam(required = false) String transactionType) {
        List<Transaction> transactions = transactionService.getAllTransaction(sortOrder, category, transactionType);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        transactionService.deleteTransaction(id);
        return "Transaction with ID " + id + " is logically delete (using_type set to 3)";

    }

}
