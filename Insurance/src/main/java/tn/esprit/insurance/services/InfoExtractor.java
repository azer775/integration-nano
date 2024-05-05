package tn.esprit.insurance.services;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.*;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.output.DoubleOutputParser;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
//import jnr.posix.WString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.langchain4j.data.message.AiMessage;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
public class InfoExtractor {


    public Object IfYouNeedSimplicity() {



            EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

            EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(300, 0))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            Document document = loadDocument(/*toPath(*/"C:\\Users\\azerb\\Desktop\\fiche-de-Paie-Excel-Tunisie.pdf"/*)*/, /*new TextDocumentParser()*/new ApachePdfBoxDocumentParser());
            ingestor.ingest(document);

       // System.out.println(document);
            ConversationalRetrievalChain chain = ConversationalRetrievalChain.builder()
                    .chatLanguageModel(OpenAiChatModel.withApiKey("demo"))
                    .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                    //.chatMemory() // you can override default chat memory
                    // .promptTemplate() // you can override default prompt template
                    .build();

            String answer = chain.execute("extract Salaire brut or Total Brut?,i insist that i only just want what you write to be a number in a form of double in java language");
            answer=answer.replaceAll("[^0-9.,]", "");
            answer=answer.replace(",",".");
            System.out.println(answer); // Charlie is a cheerful carrot living in VeggieVille...
        double test=10;
        try {
             test=Double.parseDouble(answer);
        }catch (NumberFormatException e) {
            // Gérer le cas où la chaîne n'est pas un nombre valide
            System.out.println("La chaîne ne représente pas un nombre valide.");
        }

        return test;
    }
    public Object Salaryextractor(String path1) {

        //Path path=toPath(path1);
       /* if (path1==null){
            path1="C:\\Users\\azerb\\Desktop\\fiche-de-Paie-Excel-Tunisie.pdf";
        }*/

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        Document document = loadDocument(path1, new ApachePdfBoxDocumentParser());
        ingestor.ingest(document);

        // System.out.println(document);
        ConversationalRetrievalChain chain = ConversationalRetrievalChain.builder()
                .chatLanguageModel(OpenAiChatModel.withApiKey("demo"))
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                //.chatMemory() // you can override default chat memory
                // .promptTemplate() // you can override default prompt template
                .build();

        String answer = chain.execute("extract Salaire brut or Total Brut?,i insist that i only just want what you write to be a number in a form of double in java language");
        answer=answer.replaceAll("[^0-9.,]", "");
        answer=answer.replace(",",".");
        System.out.println(answer); // Charlie is a cheerful carrot living in VeggieVille...
        double test=10;
        try {
            test=Double.parseDouble(answer);
        }catch (NumberFormatException e) {
            // Gérer le cas où la chaîne n'est pas un nombre valide
            System.out.println("La chaîne ne représente pas un nombre valide.");
        }

        return test;
    }
    public Object chat(String question) {


        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        Document document = loadDocument(/*toPath(*/"C:\\Users\\azerb\\Desktop\\fiche-de-Paie-Excel-Tunisie.pdf"/*)*/, /*new TextDocumentParser()*/new ApachePdfBoxDocumentParser());
        //ingestor.ingest(document);
        //ChatMemory chatMemory=new ChatMemory() ;
        // System.out.println(document);
        ConversationalRetrievalChain chain = ConversationalRetrievalChain.builder()
                .chatLanguageModel(OpenAiChatModel.withApiKey("demo"))
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                .chatMemory(chatMemory) // you can override default chat memory
                // .promptTemplate() // you can override default prompt template
                .build();

        String answer = chain.execute(question);



        return answer;
    }
    private static Path toPath(String fileName) {
        try {
            URL fileUrl = InfoExtractor.class.getClassLoader().getResource(fileName);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public String Simple_Prompt() {

            ChatLanguageModel model = OpenAiChatModel.withApiKey("demo");

            String joke = model.generate("are you gpt 3.5 or 4");


            System.out.println(joke);
            return joke;

    }
   public Document testpdf(){
       Document document = loadDocument("C:\\Users\\azerb\\Desktop\\Fiche-de-paie-type-1.pdf", new ApachePdfBoxDocumentParser());
        System.out.print(document);
        return document;
   }
}