package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ILLoganCountyParser extends DispatchH05Parser {

  public ILLoganCountyParser() {
    super("LOGAN COUNTY", "IL",
          "( Call_Time:DATETIME! Location:ADDRCITY! Call_Type:CODE_CALL! Response_Area:MAP! DASHES! DASHES! Status_Times:EMPTY! " +
          "| GPSCAD " +
          ") TIMES+");
  }

  @Override
  public String getFilter() {
    return "riprun@sangamonil.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DASHES")) return new SkipField("-{4,}", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("GPSCAD")) return new MyGpsCadField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" -")) field += ' ';
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(" - ");
      super.parse(p.get(),  data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\S+) - (.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern GPSCAD_PTN =
      Pattern.compile("GPSCAD\\* (.*?) *MSGCAD\\* *(?:(\\d+) - )?(.*?) ADDRESS: *([^,]*?)(?:, *(.*?))? +Common Name: *(.*?) Response Area: *(.*?) Status Times:");
  private class MyGpsCadField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('+', ' ');
      Matcher match = GPSCAD_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1), data);
      data.strCode = getOptGroup(match.group(2));
      data.strCall = match.group(3).trim();
      parseAddress(match.group(4).trim(), data);
      data.strCity = getOptGroup(match.group(5));
      data.strPlace = match.group(6).trim();
      data.strMap = match.group(7).trim();
    }

    @Override
    public String getFieldNames() {
      return "GPS CODE CALL ADDR APT CITY PLACE MAP";
    }

  }
}
