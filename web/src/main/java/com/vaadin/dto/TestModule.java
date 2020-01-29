package com.vaadin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.dto.Answer;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestModule {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private List<Answer> answers;

    public List<String> getTextForAnswers() {
        List<String> text = new ArrayList<>();
        answers.forEach(x -> text.add(x.getText()));
        return text;
    }

}
