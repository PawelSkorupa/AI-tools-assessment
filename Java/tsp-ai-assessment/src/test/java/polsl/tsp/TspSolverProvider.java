package polsl.tsp;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import polsl.tsp.complex.*;

public class TspSolverProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("ChatGPT", new ComplexChatGptSolution()),
                Arguments.of("MS Copilot", new ComplexMsCopilotSolution()),
                Arguments.of("Github Copilot", new ComplexGithubCopilotSolution()),
                Arguments.of("Claude", new ComplexClaudeSolution()),
                Arguments.of("DeepSeek BruteForce", new ComplexDeepSeekBruteForceSolution()),
                Arguments.of("DeepSeek Nearest Neighbour", new ComplexDeepSeekNNSolution()),
                Arguments.of("Google Gemini", new ComplexGeminiSolution()),
                Arguments.of("Tabnine", new ComplexTabnineSolution())
                // Add more solvers here
        );
    }
}
