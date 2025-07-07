package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALackawannaCountyDParser extends FieldProgramParser {

  public PALackawannaCountyDParser() {
    super(CITY_CODES, "LACKAWANNA COUNTY", "PA",
          "Call_Type:CALL! Location:ADDRCITY! Units:UNIT! " +
              "( Full_Transcript:INFO! INFO/N+ Cross_Streets:X " +
              "| Priority:PRI! ( Incident_Number:ID! | CAD:ID! ) Radio_Channel:CH! Area:MAP! Time:TIME! Coordinates:GPS COMMENT:INFO! INFO/N+ " +
              ") END");

  }

  @Override
  public String getFilter() {
    return "lackawannafirepager@gmail.com,alerts@cad.nepafirephotos.com";
  }

  private boolean addInfo;

  @Override
  protected boolean parseMsg(String body, Data data) {
    addInfo = true;
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID")) return new IdField("#?(F\\d+)", true);
    if (name.equals("TIME")) return new TimeField("(?:Dispatch Time )?(\\d\\d:\\d\\d)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern BAD_CITY_PTN = Pattern.compile("\\d+");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);

      // City code table is very sparse, so if we find an unknonw entry, clear it
      if (BAD_CITY_PTN.matcher(data.strCity).matches()) data.strCity = "";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "[");
      field = stripFieldEnd(field, "]");
      field = field.replace("\'", "").replace(", ", ",").replace(' ', '_');
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Group:")) {
        addInfo = false;
      }
      else if (field.equals("Comment:")) {
        addInfo = true;
      }
      else if (addInfo) {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "12", "AVOCA",
      "18", "WAPWALLOPEN",
      "24", "MOUNTAIN TOP",
      "27", "EDWARDSVILLE",
      "31", "FAIRMOUNT TWP",
      "33", "SCRANTON",
      "37", "ASHLEY",
      "39", "HAZLETON",
      "41", "HAZLE TWP",
      "48", "JENKINS TWP",
      "58", "NANTICOKE",
      "63", "GLEN LYON",
      "66", "PITSTON TWP",
      "67", "LAUREL RUN",
      "68", "BLAKELY",
      "69", "SCRANTON",
      "74", "SALEM TWP",
      "75", "SHICKSHINNY",
      "76", "NUANGOLA",
      "79", "SWOYERSVILLE",
      "87", "WILKES-BARRE",
      "88", "WILKES-BARRE TWP",
      "92", "YATESVILLE"
  });
}
