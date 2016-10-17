package net.anei.cadpage.parsers.IL;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILDuPageCountyCParser extends FieldProgramParser {

  public ILDuPageCountyCParser() {
    this("DUPAGE COUNTY", "IL");
  }
  
  protected ILDuPageCountyCParser(String defCity, String defState) {
    super(defCity, defState, 
          "DATE_TIME_ID ADDR_MAP! EMPTY+? CALL PLACE INFO/N+ CALL_RECEIVED:SKIP");
  }
  
  @Override
  public String getAliasCode() {
    return "ILDuPageCountyC";
  }
  
  @Override
  public String getLocName() {
    if (getParserCode().equals("ILDuPageCounty")) return "DuPage County, IL";
    return super.getLocName();
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("New Call/Incident Alert")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_ID")) return new MyDateTimeIdField();
    if (name.equals("ADDR_MAP")) return new MyAddressMapField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}) (\\d{1,2}:\\d{2}:\\d{2} [AP]M) (\\d+)");
  private static SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeIdField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      data.strCallId = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }
  
  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*?) *(?:MAP +PAGE:(.*?))?");
  private class MyAddressMapField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (!match.matches()) abort();  // Can't happen
      super.parse(match.group(1).trim(), data);
      data.strMap = getOptGroup(match.group(2));
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " MAP";
    }
  }
}
