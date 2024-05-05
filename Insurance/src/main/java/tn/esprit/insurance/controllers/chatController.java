package tn.esprit.insurance.controllers;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.insurance.services.Chat;
import tn.esprit.insurance.services.LoanService;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin("*")
@RequestMapping("/llama")
public class chatController {
    @Autowired
    LoanService loanService;
    @PostMapping("/chat")
    public String repondre(@RequestBody String question){

            return Chat.getInstance(loanService).repondre(question);
    }
    @PostMapping("/chat1")
    public ResponseEntity<String> repondre1(@RequestBody String question){
        String response = Chat.getInstance(loanService).repondre(question);
        return ResponseEntity.ok().body("{\"response\": \"" + response + "\"}");
    }
}
