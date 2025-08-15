package persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador genérico de arquivos JSON com Gson
 * @param <T> Tipo da entidade a ser persistida
 */
public class GerenciadorArquivos<T> {
    private final Gson gson;
    private final String nomeArquivo;
    private final Type tipoLista;
    private final String diretorioDados = "data";

    public GerenciadorArquivos(String nomeArquivo, Class<T> classeEntidade) {
        this.nomeArquivo = nomeArquivo;
        this.tipoLista = TypeToken.getParameterized(List.class, classeEntidade).getType();
        
        // Configurar Gson com adaptadores para LocalDate e LocalDateTime
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
        
        criarDiretorioSeNaoExistir();
    }

    private void criarDiretorioSeNaoExistir() {
        try {
            Path diretorio = Paths.get(diretorioDados);
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
                System.out.println("Diretório de dados criado: " + diretorio.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    public List<T> carregar() {
        try {
            File arquivo = new File(diretorioDados, nomeArquivo);
            
            if (!arquivo.exists()) {
                System.out.println("Arquivo não encontrado: " + arquivo.getPath() + ". Criando lista vazia.");
                return new ArrayList<>();
            }

            if (arquivo.length() == 0) {
                System.out.println("Arquivo vazio: " + arquivo.getPath() + ". Retornando lista vazia.");
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(arquivo)) {
                List<T> lista = gson.fromJson(reader, tipoLista);
                return lista != null ? lista : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo " + nomeArquivo + ": " + e.getMessage());
            return new ArrayList<>();
        } catch (JsonSyntaxException e) {
            System.err.println("Erro na sintaxe JSON do arquivo " + nomeArquivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean salvar(List<T> lista) {
        try {
            File arquivo = new File(diretorioDados, nomeArquivo);
            
            try (FileWriter writer = new FileWriter(arquivo)) {
                gson.toJson(lista, writer);
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo " + nomeArquivo + ": " + e.getMessage());
            return false;
        }
    }

    public boolean existe() {
        File arquivo = new File(diretorioDados, nomeArquivo);
        return arquivo.exists() && arquivo.length() > 0;
    }

    public void criarBackup() {
        try {
            File arquivo = new File(diretorioDados, nomeArquivo);
            if (arquivo.exists()) {
                String nomeBackup = nomeArquivo.replace(".json", "_backup.json");
                File backup = new File(diretorioDados, nomeBackup);
                Files.copy(arquivo.toPath(), backup.toPath());
                System.out.println("Backup criado: " + backup.getPath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar backup: " + e.getMessage());
        }
    }

    // Adaptador para LocalDate
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }

    // Adaptador para LocalDateTime
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}
