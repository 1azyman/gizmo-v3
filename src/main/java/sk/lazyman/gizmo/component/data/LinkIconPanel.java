package sk.lazyman.gizmo.component.data;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author lazyman
 */
public class LinkIconPanel extends Panel {

    private static final String ID_LINK = "link";
    private static final String ID_IMAGE = "image";

    public LinkIconPanel(String id, IModel<String> model) {
        this(id, model, null);
    }

    public LinkIconPanel(String id, IModel<String> model, IModel<String> titleModel) {
        super(id);

        initLayout(model, titleModel);
    }

    private void initLayout(IModel<String> model, IModel<String> titleModel) {
        AjaxLink link = new AjaxLink(ID_LINK) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onClickPerformed(target);
            }
        };

        Label image = new Label(ID_IMAGE);
        image.add(AttributeModifier.replace("class", model));
        if (titleModel != null) {
            image.add(AttributeModifier.replace("title", titleModel));
        }
        link.add(image);
        link.setOutputMarkupId(true);
        add(link);
    }

    protected AjaxLink getLink() {
        return (AjaxLink) get(ID_LINK);
    }

    protected void onClickPerformed(AjaxRequestTarget target) {

    }
}
