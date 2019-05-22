package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMercerCountyAParser extends FieldProgramParser {
  
  public OHMercerCountyAParser() {
    super("MERCER COUNTY", "OH", 
          "SRC CALL STATUS ADDRCITY UNIT BOX! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,4}", true);
    if (name.equals("STATUS")) return new SkipField("DIS|ENR");
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]{3,4}\\d?|SQ\\d{1,2}", true);
    if (name.equals("BOX")) return new BoxField("\\d{4}");
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("APT +(.*)|\\d{1,4}[A-Z]?|[A-Z]");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
        field = field.substring(0, pt).trim();
      }
      for (String part : field.split(";")) {
        part = part.trim();
        if (part.length() == 0) continue;
        if (data.strAddress.length() == 0) {
          parseAddress(field, data);
        } else {
          Matcher match = APT_PTN.matcher(part);
          if (match.matches()) {
            String apt = match.group(1);
            if (apt == null) apt = part;
            if (!data.strApt.contains(apt)) {
              data.strApt = append(data.strApt, "-", apt);
            }
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CEL",  "CELINA",
      "CW",   "CHICKASAW",
      "FTR",  "FORT RECOVERY",
      "MAR",  "MARION TWP",
      "MEN",  "MENDON",
      "ROC",  "ROCKFORD",
      "SPE",  "SPENCERVILLE",
      "STH",  "ST HENRY"
  });
}
