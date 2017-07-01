package Entity;

import Entity.util.JsfUtil;
import Entity.util.JsfUtil.PersistAction;
import Bean.TlUrlFormulaireSondeFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("tlUrlFormulaireSondeController")
@SessionScoped
public class TlUrlFormulaireSondeController implements Serializable {

    @EJB
    private Bean.TlUrlFormulaireSondeFacade ejbFacade;
    private List<TlUrlFormulaireSonde> items = null;
    private TlUrlFormulaireSonde selected;

    public TlUrlFormulaireSondeController() {
    }

    public TlUrlFormulaireSonde getSelected() {
        return selected;
    }

    public void setSelected(TlUrlFormulaireSonde selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TlUrlFormulaireSondeFacade getFacade() {
        return ejbFacade;
    }

    public TlUrlFormulaireSonde prepareCreate() {
        selected = new TlUrlFormulaireSonde();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TlUrlFormulaireSondeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TlUrlFormulaireSondeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TlUrlFormulaireSondeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<TlUrlFormulaireSonde> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public TlUrlFormulaireSonde getTlUrlFormulaireSonde(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<TlUrlFormulaireSonde> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TlUrlFormulaireSonde> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = TlUrlFormulaireSonde.class)
    public static class TlUrlFormulaireSondeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TlUrlFormulaireSondeController controller = (TlUrlFormulaireSondeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tlUrlFormulaireSondeController");
            return controller.getTlUrlFormulaireSonde(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TlUrlFormulaireSonde) {
                TlUrlFormulaireSonde o = (TlUrlFormulaireSonde) object;
                return getStringKey(o.getIdTlUrlFormulaireSonde());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TlUrlFormulaireSonde.class.getName()});
                return null;
            }
        }

    }

}
