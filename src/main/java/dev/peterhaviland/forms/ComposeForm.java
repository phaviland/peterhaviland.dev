package dev.peterhaviland.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ComposeForm {

    @NotNull
    @Size(min=1, max=50)
    String subject;
    
    @NotNull
    @Size(min=1, max=10000)
    String body;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
}
