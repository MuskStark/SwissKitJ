package fan.summer.kitpage.excel.worker;

import java.util.List;
import java.util.Map;

public interface ExcelAnalysisCallback {
    /**
     * Called when the Excel analysis is completed successfully
     *
     * @param result a map where keys are sheet names and values are lists of header strings
     */
    void onSuccess(Map<String, Map<Integer, String>> result);
    /**
     * Called when the Excel analysis fails
     *
     * @param e the exception that caused the failure
     */
    void onFailure(Exception e);
}