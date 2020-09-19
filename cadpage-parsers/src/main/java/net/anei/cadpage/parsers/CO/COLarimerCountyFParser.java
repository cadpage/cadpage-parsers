package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class COLarimerCountyFParser extends FieldProgramParser {

  public COLarimerCountyFParser() {
    super("LARIMER COUNTY", "CO", 
          "( SELECT/RR INFO1/N+ " +
          "| CALL! Address:ADDR! Apt:APT! Loc:PLACE! TlkGp:CH! Units:UNIT! Notes:INFO! INFO/ Lat:GPS1/d Long:GPS2/d END )");
  }
  
  private static final Pattern PREFIX = Pattern.compile("CFS[:/]z? *");
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = PREFIX.matcher(body);
    if (match.lookingAt()) {
      setBreakChar('/');
      setSelectValue("");
    } else if (body.startsWith("Transport Times:")) {
      data.msgType = MsgType.RUN_REPORT;
      setBreakChar(':');
      setSelectValue("RR");
    } else {
      return false;
    }
    
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    return super.getField(name);
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile(",?\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String delim = "; ";
      for (String line : INFO_BRK_PTN.split(field)) {
        line = line.trim();
        if (line.length() == 0) continue;
        data.strSupp = append(data.strSupp, delim, line);
        delim = "\n";
      }
    }
  }

  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Run #:")) {
        data.strCallId = field.substring(5).trim();
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ID";
    }
  }
}
