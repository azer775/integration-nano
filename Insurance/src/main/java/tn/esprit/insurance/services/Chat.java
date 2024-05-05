package tn.esprit.insurance.services;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import dev.langchain4j.service.AiServices;
import tn.esprit.insurance.entities.User;

import java.time.Duration;

public class Chat {
    private static Chat instance;
    ChatLanguageModel model;
    OllamaStreamingChatModel stream;

    ChatMemory chatMemory;
    ConversationalChain chain;


    public int nb=0;
    private final LoanService loanService;

    public static Chat getInstance(LoanService loanService) {
        if (instance == null) {
            instance = new Chat(loanService);
        }
        return instance;
    }
    private Chat(LoanService loanService){
        this.loanService= loanService;
       /* stream.builder().baseUrl("http://localhost:11434")
                .timeout(Duration.ofMinutes(20))
                .modelName("gemma:2b")
                .build();*/
        /*ollama*/
        model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .timeout(Duration.ofMinutes(20))
                .modelName("gemma:2b")
                .build();
        /*gpt */
        //model = OpenAiChatModel.withApiKey("demo");
        chatMemory= MessageWindowChatMemory.withMaxMessages(200);
        chain=ConversationalChain.builder()
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .build();

    }
    public String repondre(String question){
        String string;
        if (nb==0)
        {string ="act as you are a financial chatbot advisor  with the name sam that have the right to access the data given to you in this prompt who answers briefly and only answers to financial or banking related questions i insist financial or banking related questions and if the client ask you unrelated question you tell him that you only answer financial or banking questions and you give informations about loans from this list"+loanService.getAll();
            chain.execute(string);

        }
        String r=chain.execute(question);
        System.out.println(r);
        nb++;
        return r;

    }


}
