package sk.lazyman.gizmo.component.form;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

/**
 * @author lazyman
 */
public class AreaFormGroup<T extends Serializable> extends FormGroup<TextAreaInput, T> {

    public AreaFormGroup(String id, IModel<T> value, IModel<String> label, boolean required) {
        super(id, value, label, required);
    }

    @Override
    protected FormInput createInput(String componentId, IModel<T> model, IModel<String> placeholder) {
        TextAreaInput textInput = new TextAreaInput(componentId, model);
        FormComponent input = textInput.getFormComponent();
        input.add(AttributeAppender.replace("placeholder", placeholder));

        return textInput;
    }

    public void setRows(int rows) {
        TextAreaInput formInput = getFormInput();
        formInput.setRows(rows);
    }
}
