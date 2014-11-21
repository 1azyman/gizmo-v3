package sk.lazyman.gizmo.component;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import sk.lazyman.gizmo.data.AbstractTask;
import sk.lazyman.gizmo.data.Work;
import sk.lazyman.gizmo.dto.CustomerProjectPartDto;
import sk.lazyman.gizmo.dto.ReportSearchSummaryDto;
import sk.lazyman.gizmo.dto.WorkFilterDto;
import sk.lazyman.gizmo.util.LoadableModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lazyman
 */
public class ReportSearchSummary extends SimplePanel<ReportSearchSummaryDto> {

    private static final String ID_PROJECT = "project";
    private static final String ID_FROM = "from";
    private static final String ID_TO = "to";
    private static final String ID_INVOICE = "invoice";
    private static final String ID_WORK = "work";

    public ReportSearchSummary(String id, IModel<ReportSearchSummaryDto> model) {
        super(id, model);
        setRenderBodyOnly(true);

        initPanelLayout();
    }

    public ReportSearchSummary(String id, final IModel<WorkFilterDto> filterModel,
                               final IModel<List<AbstractTask>> dataModel) {
        this(id, new LoadableModel<ReportSearchSummaryDto>(false) {

            @Override
            protected ReportSearchSummaryDto load() {
                ReportSearchSummaryDto dto = new ReportSearchSummaryDto();

                WorkFilterDto filter = filterModel.getObject();
                dto.setProject(filter.getProject());
                dto.setFrom(filter.getFrom());
                dto.setTo(filter.getTo());

                dto.setInvoice(sumInvoiceLength(dataModel));
                dto.setWork(sumWorkLength(dataModel));

                return dto;
            }
        });
    }

    private void initPanelLayout() {
        Label project = new Label(ID_PROJECT, createProjectModel());
        add(project);

        Label from = new Label(ID_FROM,
                createStringDateModel(new PropertyModel<Date>(getModel(), ReportSearchSummaryDto.F_FROM)));
        add(from);

        Label to = new Label(ID_TO,
                createStringDateModel(new PropertyModel<Date>(getModel(), ReportSearchSummaryDto.F_TO)));
        add(to);

        Label invoice = new Label(ID_INVOICE,
                createHourMDModel(new PropertyModel<Double>(getModelObject(), ReportSearchSummaryDto.F_INVOICE)));
        invoice.setRenderBodyOnly(true);
        add(invoice);

        Label work = new Label(ID_WORK,
                createHourMDModel(new PropertyModel<Double>(getModelObject(), ReportSearchSummaryDto.F_WORK)));
        work.setRenderBodyOnly(true);
        add(work);
    }

    private IModel<String> createStringDateModel(final IModel<Date> dateModel) {
        return new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                Date date = dateModel.getObject();
                if (date == null) {
                    return null;
                }

                DateFormat df = new SimpleDateFormat("EEE dd. MMM. yyyy");
                return df.format(date);
            }
        };
    }

    private IModel<String> createProjectModel() {
        return new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                ReportSearchSummaryDto dto = getModelObject();
                CustomerProjectPartDto cppDto = dto.getProject();
                return PartAutoCompleteConverter.convertToString(cppDto);
            }
        };
    }

    private IModel<String> createHourMDModel(final IModel<Double> model) {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return createHourMd(model.getObject());
            }
        };
    }

    private String createHourMd(double hours) {
        return StringUtils.join(new Object[]{hours, "/", hours / 8});
    }

    private static double sumInvoiceLength(final IModel<List<AbstractTask>> data) {
        List<AbstractTask> list = data.getObject();

        double sum = 0;
        for (AbstractTask task : list) {
            if (task instanceof Work) {
                sum += ((Work) task).getInvoiceLength();
            }
        }
        return sum;
    }

    private static double sumWorkLength(final IModel<List<AbstractTask>> data) {
        List<AbstractTask> list = data.getObject();

        double sum = 0;
        for (AbstractTask task : list) {
            sum += task.getWorkLength();
        }
        return sum;
    }
}
