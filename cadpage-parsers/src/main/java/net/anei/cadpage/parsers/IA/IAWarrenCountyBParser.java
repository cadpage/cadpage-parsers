package net.anei.cadpage.parsers.IA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IAWarrenCountyBParser extends FieldProgramParser {

  public IAWarrenCountyBParser() {
    this("WARREN COUNTY", "IA");
  }

  IAWarrenCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "Jurisdiction:SRC? Inci.#:ID! Time:DATETIME! Inci.Type:CALL! Address:ADDR! Bldg:APT! Unit/Apt:APT! City:CITY! xStreet:X! Loc.Name:PLACE! Area:MAP! " +
              "Lat/Long:GPS/d! Map_Zone:MAP/L! ( Resp._Chnl:CH! | Cmd_Chnl:CH! ) Tac_Chnl:CH/L! Units:UNIT! Comments:INFO! END");
  }

  @Override
  public String getAliasCode() {
    return "IAWarrenCountyB";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MISSING_BLNK_PTN = Pattern.compile("(?<![\\s\\[])(?=(?:Inci\\.#|Address|Bldg|Unit/Apt|City|xstreet|Loc\\.Name|Lat/Long|Area|Map Zone|Resp\\. Chnl|Tac Chnl|Units|Comments):)");
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Incident Page:");
    body = body.replace("Jurisdicition:", "Jurisdiction:")
               .replace("Inci. Type:", "Inci.Type:")
               .replace("Unit:", "Units:");
    body = MISSING_BLNK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]+(?=\\[\\d{1,2}\\])");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
