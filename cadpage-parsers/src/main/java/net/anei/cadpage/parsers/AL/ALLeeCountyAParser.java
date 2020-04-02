package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Lee County, AL
 */
public class ALLeeCountyAParser extends FieldProgramParser {  
  public ALLeeCountyAParser() {
    super(ALLeeCountyParser.CITY_LIST, "LEE COUNTY", "AL",
          "ADDR/S5XP PLACE? ( X ID | ID ) CALL! geo:GPS? INFO/N+");
    removeWords("CIRCLE");
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(\\d{4}-\\d+);(.*)\\|");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = stripFieldStart(body, "CFS:");
    
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).replace('|', '\n').trim();
      return true;
    }
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d+", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern SECTOR_PTN = Pattern.compile(" - [NSEW] SECTOR\\b");
  private static final Pattern ADDRESS_PATTERN  = Pattern.compile("(.*?LEE RD +\\d+ +)(\\d+)(.*)");
  private static final Pattern LEAD_NUM_PTN = Pattern.compile("(\\d+)\\b *(.*)");
  private static final Pattern LEAD_DIR_PTN = Pattern.compile("([NSEW]B?)\\b *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      
      field = field.replace("\\", "&");
      field = SECTOR_PTN.matcher(field).replaceAll("");
      
      Matcher m = ADDRESS_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1)+m.group(3);
        data.strApt = m.group(2);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0) {
        if (data.strAddress.toUpperCase().endsWith("LEE RD")) {
          if (data.strApt.length() > 0) {
            data.strAddress = append(data.strAddress, " ", data.strApt);
            data.strApt = "";
          } else {
            m = LEAD_NUM_PTN.matcher(data.strPlace);
            if (m.matches()) {
              data.strAddress = append(data.strAddress, " ", m.group(1));
              data.strPlace = m.group(2);
            }
          }
        }
        m = LEAD_DIR_PTN.matcher(data.strPlace);
        if (m.matches()) {
          data.strAddress = append(data.strAddress, " ", m.group(1));
          data.strPlace = m.group(2);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" APT";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("X ")) {
        field = field.substring(2);
      } else if (field.endsWith(" X")) {
        field = field.substring(0, field.length()-2);
      } else if (field.contains(" X ")) {
        field = field.replace(" X ", " / ");
      } else return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern CALL_PATTERN = Pattern.compile("(\\d{2}-\\d{2}) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = CALL_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCode = m.group(1);
        data.strCall = m.group(2);
      }
      else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CODE";
    }
  }
}
