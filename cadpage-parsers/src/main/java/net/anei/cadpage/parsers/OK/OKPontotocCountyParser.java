package net.anei.cadpage.parsers.OK;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OKPontotocCountyParser extends FieldProgramParser {
  
  public OKPontotocCountyParser() {
    super("PONTOTOC COUNTY", "OK",
          "CALL ADDR! Call_Received_Time:DATETIME1! Dispatch:DATETIME2");
    setupSpecialStreets(
        "ARLINGTON CENTER",
        "BROADWAY"
     );
  }
  
  @Override
  public String getFilter() {
    return "ada911@versatilenetworks.com,paging@adaok.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("DispatchAlert")) return false;
    
    String[] flds = body.split("\n");
    if (flds.length > 1) return parseFields(body.split("\n"), data);
    
    // Not a normal dispatch alert, but see if we can find an address in it
    parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT, body, data);
    if (!isValidAddress()) return false;
    setFieldList("CALL ADDR APT INFO");
    String left = getLeft();
    if (data.strCall.length() == 0) {
      data.strCall = left;
    } else {
      data.strSupp = left;
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new MyDateTimeField(true);
    if (name.equals("DATETIME2")) return new MyDateTimeField(false);
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    
    private boolean required;
    
    public MyDateTimeField(boolean required) {
      this.required = required;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data) && required) abort();
    }
  }
  
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("SH3W", "OK 3W");
  }
}
