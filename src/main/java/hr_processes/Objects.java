package hr_processes;

import org.openqa.selenium.By;

public class Objects {
	
	static By txt_username = By.id("userid");
	static By txt_password = By.id("pwd");
	static By txt_headcount_run_cnrl_id = By.id("Z_HEADCNT_RUN_RUN_CNTL_ID");
	static By btn_headcount_run_cnrl_id_search = By.id("#ICSearch");
	static By btn_headcount_ok = By.id("#ICSave");
	static By btn_headcount_run = By.id("PRCSRQSTDLG_WRK_LOADPRCSRQSTDLGPB");
	static By btn_headcount_ProcessMonitor = By.id("PRCSRQSTDLG_WRK_LOADPRCSMONITORPB");
	static By txt_headcount_asofdate = By.id("Z_HEADCNT_RUN_ASOFDATE");
	static By btn_login = By.name("Submit");
	static By txt_query_search = By.name("QRYSELECT_WRK_QRYSEARCHTEXT254");
	static By btn_query_search = By.name("QRYSELECT_WRK_QRYSEARCHBTN");
	static By btn_query_edit = By.name("QRYSELECT_WRK_QRYEDITFIELD$0");
	static By btn_run_query = By.xpath("//*[@id=\"PSTAB\"]/table/tbody/tr/td[10]/a");
    static String frame = "ptifrmtgtframe";
    static By txt_template_id = By.id("HR_TBH_WRK_TBH_TMPL_ID");
    static By btn_template_continue = By.id("HR_TBH_WRK_TBH_ADD");
    static By lbl_query_name = By.id("QRYPROP_WRK_QRYNAMETEXT");
    static By lbl_column_number = By.xpath("//*[@id=\"PSCENTER\"]/table/tbody/tr/td/span[3]");
    static By txt_reason_code = By.id("HR_TBH_WRK_ACTION_REASON");
    static By btn_reason_code_continue = By.id("HR_TBH_WRK_TBH_NEXT");
    static String Education = "Highest Education Level";
    static String Marital = "Marital Status";
    static String NationalIDType = "National ID Type";
    static String NationalID = "National ID:";
    static String Telephone = "Telephone";
    static String Email = "Email Address";
    static String Probation = "Probation Date";
    static String Classification = "Employee Classification:";
    static String Schedule = "Holiday Schedule";
    static String PositionNumber = "Position Number";
    static String PayGroup = "Pay Group";
    static String SalaryGrade = "Salary Grade";
    static String City = "City";
    static String State = "State";
}
