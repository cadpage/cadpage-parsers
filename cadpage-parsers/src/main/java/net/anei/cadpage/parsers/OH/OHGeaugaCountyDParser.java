package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHGeaugaCountyDParser extends FieldProgramParser {

  public OHGeaugaCountyDParser() {
    super("GEAUGA COUNTY", "OH",
          "CADNUMBER:ID! NATURE:CALL! ADDR:ADDR! PLACE:PLACE! AGENCYIR:CH! CITY:CITY! STATE:ST! AGENCY:SRC! ZONE:MAP! CFS:ID/L! " +
              "LONGITUDE:GPS1! LATITUDE:GPS2! UNITS:UNIT! INFO:INFO INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d?:\\d\\d:\\d\\d \\d\\d?/\\d\\d?/\\d{4} - .*|.*Est. Loc.:.*");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("Phone: *(\\S*),");
  private static final Pattern INFO_NAME_PTN = Pattern.compile("Caller Name: *(.*?),");
  private static final Pattern INFO_MSPACE_PTN = Pattern.compile(" {2,}");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.matches()) return;

      if (field.startsWith("WPH2 data.")) {
        match = INFO_PHONE_PTN.matcher(field);
        if (match.find()) data.strPhone = match.group(1);
        return;
      }

      if (field.startsWith("VOIP data.")) {
        match = INFO_NAME_PTN.matcher(field);
        if (match.find()) data.strName = cleanWirelessCarrier(match.group(1).trim());
        return;
      }

      field = INFO_MSPACE_PTN.matcher(field).replaceAll(" ");
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE INFO";
    }
  }
}
