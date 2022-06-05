package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.Cadpage2Parser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyGParser extends Cadpage2Parser {

  public WASnohomishCountyGParser() {
    super("SNOHOMISH COUNTY", "WA");
    setMap("ID", "Active911#");
    setFieldList("DATE TIME CALL ADDR APT CITY MAP PLACE CH SRC UNIT GPS ID INFO");
  }

  @Override
  public String getFilter() {
    return "noreply@alert.active911.com";
  }

  @Override
  public Field getMapField(String name) {
    if (name.equals("Active911:")) return new IdField();
    return super.getMapField(name);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }

  }

}
