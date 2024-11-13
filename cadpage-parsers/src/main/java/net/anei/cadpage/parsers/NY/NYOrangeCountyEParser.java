package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYOrangeCountyEParser extends FieldProgramParser {

  public NYOrangeCountyEParser() {
    super("ORANGE COUNTY", "NY",
          "Incident:EMPTY! Nature:CALL! Address:ADDRCITY! Priority:PRI! Coordinates:GPS? ID:ID? Units:UNIT? Comments:EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*) +CAD Incident");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);

    body = body.replace(": : ", ": ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", United States");
      if (field.endsWith(", New York")) {
        data.strState = "NY";
        field = field.substring(0,field.length()-11).trim();
      }
      super.parse(field, data);
      data.strCity = convertCodes(data.strCity, ZIP_TABLE);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private static final Pattern DASHES_PTN = Pattern.compile("-+");
  private static final Pattern INFO_PTN = Pattern.compile("(?:message: *)?(?:\\[\\d{1,2}\\] *)*(.*?)(?: *\\[Shared\\])?(?: +\\d{4}-\\d\\d?-\\d\\d? \\d\\d:\\d\\d:\\d\\d(?:\\.\\d+)?)?");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (DASHES_PTN.matcher(field).matches()) return;

      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Properties ZIP_TABLE = buildCodeTable(new String[] {
      "10928", "HIGHLAND FALLS",
      "10940", "MIDDLETOWN",
      "10941", "MIDDLETOWN",
      "10969", "PINE ISLAND",
      "10973", "SLATE HILL",
      "10998", "WESTTOWN",
      "12543", "MAYBROOK",
      "12549", "MONTGOMERY",
      "12550", "NEWBURGH",
      "12566", "PINE BUSH",
      "12575", "ROCK TAVERN",
      "12586", "WALDEN",
      "12589", "WALLKILL",
  });
}
