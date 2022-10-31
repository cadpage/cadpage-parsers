package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

/**
 * Henry County, IN
 */
public class INHenryCountyParser extends FieldProgramParser {

  public INHenryCountyParser() {
    super("HENRY COUNTY", "IN",
          "CALL ADDRCITY! X GPS UNIT ID INFO/N+");
  }

  @Override
  public String getFilter() {
    return "active911@henrycounty.in.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("\\|");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.contains("Active 911") && !subject.equals("DISREGARD")) return false;
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      data.strPlace = p.getLastOptional(';');
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CITY";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d .*:");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}