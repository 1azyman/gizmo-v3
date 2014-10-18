package sk.lazyman.gizmo.web.app;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.AjaxBootstrapTabbedPanel;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;
import sk.lazyman.gizmo.component.AjaxButton;
import sk.lazyman.gizmo.component.AjaxSubmitButton;
import sk.lazyman.gizmo.component.form.AreaFormGroup;
import sk.lazyman.gizmo.component.form.CheckFormGroup;
import sk.lazyman.gizmo.component.form.FormGroup;
import sk.lazyman.gizmo.data.Project;
import sk.lazyman.gizmo.repository.ProjectRepository;
import sk.lazyman.gizmo.util.LoadableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyman
 */
@MountPath("/app/project")
public class PageProject extends PageAppProjects {

    public static final String PROJECT_ID = "projectId";

    private static final String ID_FORM = "form";
    private static final String ID_NAME = "name";
    private static final String ID_DESCRIPTION = "description";
    private static final String ID_CUSTOMER = "customer";
    private static final String ID_CLOSED = "closed";
    private static final String ID_COMMERCIAL = "commercial";
    private static final String ID_CANCEL = "cancel";
    private static final String ID_SAVE = "save";
    private static final String ID_TABS = "tabs";

    private IModel<Project> model;

    public PageProject() {
        model = new LoadableModel<Project>() {

            @Override
            protected Project load() {
                return loadProject();
            }
        };

        initLayout();
    }

    private Project loadProject() {
        PageParameters params = getPageParameters();
        StringValue val = params.get(PROJECT_ID);
        String projectId = val != null ? val.toString() : null;

        if (projectId == null || !projectId.matches("[0-9]+")) {
            return new Project();
        }

        ProjectRepository repository = getProjectRepository();
        Project project = repository.findOne(Integer.parseInt(projectId));
        if (project == null) {
            getSession().error(translateString("Message.couldntFindProject", projectId));
            throw new RestartResponseException(PageProject.class);
        }

        return project;
    }

    private void initLayout() {
        Form form = new Form(ID_FORM);
        add(form);

        FormGroup name = new FormGroup(ID_NAME, new PropertyModel<String>(model, Project.F_NAME),
                createStringResource("Project.name"), true);
        form.add(name);

        FormGroup description = new AreaFormGroup(ID_DESCRIPTION, new PropertyModel<String>(model, Project.F_DESCRIPTION),
                createStringResource("Project.description"), true);
        form.add(description);

        FormGroup customer = new FormGroup(ID_CUSTOMER, new PropertyModel<String>(model, Project.F_CUSTOMER),
                createStringResource("Project.customer"), true);
        form.add(customer);

        FormGroup closed = new CheckFormGroup(ID_CLOSED, new PropertyModel<Boolean>(model, Project.F_CLOSED),
                createStringResource("Project.closed"), true);
        form.add(closed);

        FormGroup commercial = new CheckFormGroup(ID_COMMERCIAL, new PropertyModel<Boolean>(model, Project.F_COMMERCIAL),
                createStringResource("Project.commercial"), true);
        form.add(commercial);

        initButtons(form);

        List<ITab> tabList = new ArrayList<>();
        tabList.add(new AbstractTab(createStringResource("PageProject.parts")) {

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new WebMarkupContainer(panelId);
            }
        });

        AjaxBootstrapTabbedPanel tabs = new AjaxBootstrapTabbedPanel(ID_TABS, tabList);
        add(tabs);
    }

    private void initButtons(Form form) {
        AjaxSubmitButton save = new AjaxSubmitButton(ID_SAVE, createStringResource("GizmoApplication.button.save")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                savePerformed(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(form);
            }
        };
        form.add(save);

        AjaxButton cancel = new AjaxButton(ID_CANCEL, createStringResource("GizmoApplication.button.cancel")) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                cancelPerformed(target);
            }
        };
        form.add(cancel);
    }

    private void cancelPerformed(AjaxRequestTarget target) {
        setResponsePage(PageProjects.class);
    }

    private void savePerformed(AjaxRequestTarget target) {
        //todo implement
    }
}
