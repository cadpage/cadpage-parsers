package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHHamiltonCountyBParser extends FieldProgramParser {
 
  public OHHamiltonCountyBParser() {
    super("HAMILTON COUNTY", "OH",
          "DATETIME CALL! ADDR CITY INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "norwoodpolice@norwoodpolice.org,pager@amberleyvillage.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern COMMA_DATE_PTN = Pattern.compile(",(?= *\\d{4}/\\d\\d/\\d\\d \\d\\d:\\d\\d\\b)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Dispatched")) return false;
    
    String[] flds;
    body = COMMA_DATE_PTN.matcher(body).replaceAll("\n");
    int pt = body.indexOf('\n');
    if (pt < 0) {
      flds = body.split(",");
    } else {
      String[] flds1 = body.substring(0,pt).trim().split(",");
      String[] flds2 = body.substring(pt+1).trim().split("\n");
      flds = new String[flds1.length + flds2.length];
      System.arraycopy(flds1, 0, flds, 0, flds1.length);
      System.arraycopy(flds2, 0, flds, flds1.length, flds2.length);
    }
    
    return parseFields(flds, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_DATE_TIME = Pattern.compile("\\d{4}/\\d\\d/\\d\\d \\d\\d:\\d\\d\\b *(?:[A-Z][a-z]+ *, [A-Z][a-z]+\\b *(?:D\\d+|5J[A-Z0-9]+\\b)?)?[ :]*");
  private static final Pattern INFO_UNIT_PTN = Pattern.compile("( *\\b[A-Z]+\\d+)+$");
  private static final Pattern INFO_ID_PTN = Pattern.compile("[ \\.]*\\bRMS INCIDENT #(\\d+)$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = INFO_DATE_TIME.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      
      match = INFO_UNIT_PTN.matcher(field);
      if (match.find()) {
        data.strUnit = match.group().trim();
        field = field.substring(0,match.start()).trim();
      }
      
      match = INFO_ID_PTN.matcher(field);
      if (match.find()) {
        data.strCallId = match.group(1);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO ID UNIT";
    }
  }
}
