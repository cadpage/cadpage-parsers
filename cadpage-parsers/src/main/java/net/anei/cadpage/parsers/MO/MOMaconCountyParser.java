package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Macon County, MO
 */
public class MOMaconCountyParser extends FieldProgramParser {
  
  public MOMaconCountyParser() {
    super("MACON COUNTY", "MO",
      "Event_Number:ID! Police_Event_Type:CALL! Fire_Event_Type:CALL! EMS_Event_Type:CALL! Latitude:GPS1! Longitude:GPS2! Address:ADDR! Location:APT_PLACE? City:CITY! Zip:SKIP! ESN:SKIP! Class:SKIP! District:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "noreply@macondomain.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active 911")) return false;
    return super.parseFields(body.split("\n"), 12, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}-\\d+", true);
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) *(.*)");
  private class MyAptPlaceField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }
}
