package com.powerpuff.billServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerpuff.billServer.model.Message;
import com.powerpuff.billServer.model.Transaction;
import com.powerpuff.billServer.model.UsingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.io.IOException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OpenAIService {

    private static final Logger log = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OpenAIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    public JSONObject callOpenAI(String userMessage) throws Exception {
        try {
        // 创建请求体
            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("temperature", 0.5);
//            requestBody.put("max_tokens", 150);
//            requestBody.put("prompt", buildPrompt(userMessage));
            requestBody.put("model", model);
            requestBody.put("messages", new Message[]{
                    new Message("system", "You are an AI assistant specialized in recording transactions for a personal finance app."),
                    new Message("user", buildPrompt(userMessage))
            });


            // 将请求体转换为 JSON 字符串
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // 检查响应状态
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to call OpenAI API: " + response.getStatusCode() + " " + response.getBody());
            }

            // 解析返回结果
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
//            return jsonResponse.get("choices")
//                    .get(0)
//                    .get("message")
//                    .get("content")
//                    .asText()
//                    .trim();
            return new JSONObject(jsonResponse.get("choices")
                    .get(0)
                    .get("message")
                    .get("content").asText()
                    .trim());
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: ", e);
            throw new RuntimeException("Error processing JSON request", e);
        }
    }

    private String buildPrompt(String userMessage) {
        return  "Your task is to analyze the given expense description or image, and categorize it according to predefined categories. "
                + "You should also extract the amount, the currency, the date of the expense if available, and the location if provided.\n\n"
                + "The content output by the user is marked as <content></content>. Please analyze the input content and complete the task based on the following requirements:\n"
                + "1. Identify the most likely intention of the customer and record it as transaction_type;\n"
                + "2. Extract valuable information from <content> and summarize it in a short sentence, recording it as <description>;\n"
                + "3. According to the extracted intent, further extract specific information based on the following requirements, and organize the above <description> together into JSON format for return;\n"
                + "4. For the date (date) and time (time) fields in JSON, please combine the current time <current_time> to process it into the corresponding \"YYYY-MM-DD\" format and \"HH:MM\" format.\n"
                + "5. No need to return the test process; just return the JSON result obtained from the final analysis. Do not return any other content, including comments, and ensure that the final return format is in JSON format.\n\n"
                + "The logic of quality judgment and processing is as follows:\n"
                + "1. If the customer mentions a transaction, consumption, or order, it means they want to record an expenditure (outcome). Please extract specific information from the content, including date, location, amount, participants, purpose (description), and payment method (payment_method). Analyze which category the expenditure belongs to in combination with the expenditure classification list (<category_list>), recorded as category_en, such as dining consumption record FOOD_AND_DINING.\n\n"
                + "<category_list>\n"
                + "FOOD_AND_DINING,\n"
                + "SHOPPING,\n"
                + "TRANSPORTATION,\n"
                + "HOUSING,\n"
                + "ENTERTAINMENT_AND_RECREATION,\n"
                + "HEALTH_AND_INSURANCE,\n"
                + "EDUCATION_AND_TRAINING,\n"
                + "COMMUNICATION_AND_TECHNOLOGY,\n"
                + "GIFTS_AND_DONATIONS,\n"
                + "FAMILY_OBLIGATIONS,\n"
                + "DEBT_AND_SAVINGS,\n"
                + "TAXES,\n"
                + "OTHER_EXPENSE\n"
                + "</category_list>\n\n"
                + "Please return in JSON format. For example, when the content is \"Bought groceries at Walmart for $72.50 on 08/15/2024\", the final output should be in the following JSON format:\n"
                + "{\n"
                + "  \"transaction_type\": \"outcome\",\n"
                + "  \"date\": \"2024-08-15\",\n"
                + "  \"time\": \"18:30\",\n"
                + "  \"amount\": 72.50,\n"
                + "  \"currency\": \"USD\",\n"
                + "  \"category\": \"SHOPPING\",\n"
                + "  \"description\": \"Bought groceries at Walmart\",\n"
                + "  \"participants\": null,\n"
                + "  \"location\": \"Walmart\",\n"
                + "  \"payment_method\": null\n"
                + "}\n\n"
                + "2. If the customer tells me an income or funds coming in, it means that he wants to record an income (income). Please extract specific information from the content, including date (date), amount (amount), reason for income (description), income type (income_type), and payment method (payment_method). Please analyze which category the expenditure belongs to in conjunction with the income category list (<income_category_list>), and record it as category_en.\n\n"
                + "<income_category_list>\n"
                + "Salary\n"
                + "Bonuses and Allowances\n"
                + "Investment Income\n"
                + "Side Income\n"
                + "Gifts and Inheritance\n"
                + "Other Income\n"
                + "</income_category_list>\n\n"
                + "Please return in JSON format. For example, when the content is \"Got Bonuses and Allowances for $320 on 08/15/2024\", the final output should be in the following JSON format:\n"
                + "{\n"
                + "  \"transaction_type\": \"income\",\n"
                + "  \"date\": \"2024-08-15\",\n"
                + "  \"time\": \"18:30\",\n"
                + "  \"amount\": 320,\n"
                + "  \"currency\": \"USD\",\n"
                + "  \"category\": \"Bonuses and Allowances\",\n"
                + "  \"description\": \"Got Bonuses and Allowances\",\n"
                + "  \"participants\": null,\n"
                + "  \"location\": null,\n"
                + "  \"payment_method\": null\n"
                + "}\"\n"
                + "<content>" + userMessage + "</content>\n"
                + "<current_time>" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</current_time>";
    }


    // Process the uploaded image
    public JSONObject processImage(MultipartFile image) throws Exception {
        // Convert the file to base64 or upload it to a server if needed
        String imgUrl = uploadImageToStorage(image);  // This is only needed if you upload base64 encoded images.

        // Call OpenAI API or another image recognition service
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", Arrays.asList(
                new HashMap<String, Object>() {{
                    put("role", "user");
                    put("content", Arrays.asList(
                            new HashMap<String, Object>() {{
                                put("type", "text");
                                put("text", buildPrompt(""));
                            }},
                            new HashMap<String, Object>() {{
                                put("type", "image_url");  // Correct content type for image
                                // Directly using an image URL for testing purposes
                                put("image_url", new HashMap<String, Object>() {{
                                    put("url", imgUrl);
//                                    put("url", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg");
                                }});
                            }}
                    ));
                }}
        ));

        // Send the request
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // Parse the response
            JsonNode aiResponse = objectMapper.readTree(response.getBody());

            // Ensure the response contains 'choices' and get the 'content' from the first choice
            if (aiResponse.has("choices") && aiResponse.get("choices").isArray()) {
                JsonNode firstChoice = aiResponse.get("choices").get(0);
                JsonNode message = firstChoice.get("message");

                if (message != null && message.has("content")) {
                    String content = message.get("content").asText().trim();

                    // Check if the content starts with ```json\n and ends with ```
                    if (content.startsWith("```json\n") && content.endsWith("```")) {
                        // Remove the ```json\n at the beginning and the ``` at the end
                        content = content.substring(8, content.length() - 3).trim();
                    }

                    // Return the content as a JSONObject
                    return new JSONObject(content);
                } else {
                    throw new RuntimeException("No 'content' field found in response.");
                }
            } else {
                throw new RuntimeException("Invalid API response: 'choices' not found.");
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error from OpenAI API: " + e.getMessage());
            throw new Exception("Error from OpenAI API: " + e.getMessage());
        }
    }


    private String uploadImageToStorage(MultipartFile file) throws IOException {
        // This should include the logic to upload the image to your own server or cloud storage
        // Return the URL of the uploaded image
//        String imageUrl = "https://yourserver.com/uploaded_images/" + file.getOriginalFilename();
        //TODO file server
        String imageUrl = "https://i.postimg.cc/ryJ332Lw/IMG-0626.jpg";

        // Assume the upload was successful and return the URL
        // The actual upload logic should be implemented based on your specific needs, e.g., using AWS S3, Azure Blob Storage, or other methods

        return imageUrl;
    }

    // Convert the file to base64 string (you may need to implement the actual conversion)
    public String convertFileToBase64(MultipartFile file) throws IOException {
        // 1. Get the byte data of the file
        byte[] fileBytes = file.getBytes();

        // 2. Convert the byte array to a base64 string
        return Base64.getEncoder().encodeToString(fileBytes);
    }


    public String storeImage(MultipartFile image) throws IOException {
        String imgUrl = uploadImageToStorage(image);
        // Define the directory to store images (ensure you have the directory created)
        // TODO file server
//        String directory = "path/to/store/images/";
//        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
//        String filePath = directory + fileName;

        // Save the image to the server
        File file = new File(imgUrl);
        image.transferTo(file);

        return imgUrl;  // Return the path to store in the transaction

//        http://your-server/images/filename.jpg
    }

    // Parse and save the transaction based on AI response
    public Transaction parseAndSaveTransaction(JSONObject openAIResponse) {
        Transaction transaction = new Transaction();

        // Parsing JSON data
        transaction.setTransactionType(openAIResponse.getString("transaction_type"));
        transaction.setTransactionAt(LocalDateTime.parse(openAIResponse.getString("date") + "T" + openAIResponse.getString("time")));
        transaction.setTotalAmount(BigDecimal.valueOf(openAIResponse.getDouble("amount")));
        transaction.setCurrency(openAIResponse.getString("currency"));
        transaction.setCategory(openAIResponse.getString("category"));
        transaction.setDescription(openAIResponse.getString("description"));
        transaction.setCounterpartyName(openAIResponse.optString("location"));
        transaction.setPaymentMethod(openAIResponse.optString("payment_method"));

        // Set other fields as needed (createdAt, updatedAt, etc.)
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        // Set the 'isRecurring' and 'isReimbursable' fields to default values if not provided
        transaction.setIsRecurring(false); // Default value is false
        transaction.setIsReimbursable(false); // Default value is false

        //TODO usingType,userId
        transaction.setUsingType(UsingType.ACTIVE);
        transaction.setUserId(1);


        return transaction;
    }


}
