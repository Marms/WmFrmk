package bo;

import com.wm.lang.ns.NSName;

import frmk.FData;
import lombok.Data;

/**
 * TODO description de la classe
 * @author florent
 *
 */
@Data
public class Mock {
    /**
     * le nom du service a mocker
     */
    String name;
    
    /**
     * Pipeline en entree
     */
    FData pipelineIn;
    
    /**
     * Pipeline en sortie
     */
    FData pipelienOut;
    
    /**
     * La condition que le pipeline en entree doit valider 
     * pour mocker
     */
    //TODO changer par un object condition
    String conditionRegex;
    
    Integer numberOfMock;


    /**
     * @return the pipelineIn
     */
    public FData getPipelineIn() {
        return pipelineIn;
    }

    /**
     * @param pipelineIn the pipelineIn to set
     */
    public void setPipelineIn(FData pipelineIn) {
        this.pipelineIn = pipelineIn;
    }

    /**
     * @return the pipelienOut
     */
    public FData getPipelienOut() {
        return pipelienOut;
    }

    /**
     * @param pipelienOut the pipelienOut to set
     */
    public void setPipelienOut(FData pipelienOut) {
        this.pipelienOut = pipelienOut;
    }

    /**
     * @return the conditionRegex
     */
    public String getConditionRegex() {
        return conditionRegex;
    }

    /**
     * @param conditionRegex the conditionRegex to set
     */
    public void setConditionRegex(String conditionRegex) {
        this.conditionRegex = conditionRegex;
    }

    /**
     * @return the numberOfMock
     */
    public Integer getNumberOfMock() {
        return numberOfMock;
    }

    /**
     * @param numberOfMock the numberOfMock to set
     */
    public void setNumberOfMock(Integer numberOfMock) {
        this.numberOfMock = numberOfMock;
    }
}
