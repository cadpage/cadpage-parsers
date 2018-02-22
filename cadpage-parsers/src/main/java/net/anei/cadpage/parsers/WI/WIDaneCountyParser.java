package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class WIDaneCountyParser extends FieldProgramParser {

  public WIDaneCountyParser() {
    super("DANE", "WI", 
          "UNIT! Add:ADDR! Loc:PLACE! Zip:CITY! Nature:CODE_CALL_ID! Lat:GPS1! Long:GPS2! Xst:X! PDC:SKIP! Units:SKIP! END");
  }
  
  @Override
  public String getFilter() {
    return "tritech@countyofdane.com";
  }
  
  private static Pattern MASTER = Pattern.compile("Response Info Page: Municipality (?:[A-Z]+-(.*?) (?:City|Town|Village) )?(?:(.*) )?At +(.*?) +cross of(?: +(.*?))? +a +(.*?) +response for[ \\.]+(?:(\\d\\w+)-)?(.*?) *\\. Units assigned to alarm: +(.*)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Paging Message from VisiCAD")) return false;
    
    //parse in one swoop
    Matcher mat = MASTER.matcher(body);
    if (mat.matches()) {
      
      setFieldList("CITY PLACE ADDR APT X PRI CODE CALL UNIT");
      
      data.strCity = getOptGroup(mat.group(1));
      data.strPlace = getOptGroup(mat.group(2));
      parseAddress(mat.group(3).trim(), data);
      data.strCross = getOptGroup(mat.group(4));
      data.strPriority = mat.group(5);
      data.strCode = getOptGroup(mat.group(6));
      data.strCall = mat.group(7);
      data.strUnit = mat.group(8);
      
      //CLean up cross street info
      data.strCross = stripFieldStart(data.strCross, "NO X STREET");
      data.strCross = stripFieldStart(data.strCross, "/");
      data.strCross = stripFieldEnd(data.strCross, "NO X STREET");
      data.strCross = stripFieldEnd(data.strCross, "/");
      
      return true;
    }
    
    body = body.replace("Add:", " Add:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL_ID")) return new MyCodeCallIdField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
      
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_ID_PTN = Pattern.compile("(?:(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)-|\\.)?(.*?)Inc# *(\\d{2}-\\d+)");
  private class MyCodeCallIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = getOptGroup(match.group(1));
      data.strCall = match.group(2).trim();
      data.strCallId = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL ID";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("(\\d{2,})(\\d{6})");
  private class MyGPSField extends GPSField {
    
    public MyGPSField(int type) {
      super(type);
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return;
      super.parse(match.group(1)+'.'+match.group(2), data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = USH_PTN.matcher(addr).replaceAll("US");
    addr = CTH_PTN.matcher(addr).replaceAll("COUNTY ROAD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern USH_PTN = Pattern.compile("\\bUSH\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CTH_PTN = Pattern.compile("\\bCTH\\b", Pattern.CASE_INSENSITIVE);
  
}
