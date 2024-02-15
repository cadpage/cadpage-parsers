package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCHarnettCountyDParser extends FieldProgramParser {

  public NCHarnettCountyDParser() {
    super("HARNETT COUNTY", "NC",
          "( PN:CALL! UNIT(S):UNIT ADD:ADDR! ( NOTES:INFO! OCA:ID! " +
                                            "| CITY:CITY! XST:X! LT/LNG:GPS/d TAC:CH INC#:ID ( UNITS:UNIT | UNIT(S):UNIT ) TIME:TIME NOTES:INFO RA:CH/L " +
                                            ") " +
          "| RA:SRC! ( UNIT(S):UNIT! PN:CALL! ADD:ADDR! CITY:CITY! ( XST:X! TAC:CH! | TAC:CH! XST:X! | ) INC#:ID! TIME:TIME! ( NOTES:INFO! | CMT:INFO! ) " +
                    "| PN:CALL! ADD:ADDR! CITY:CITY! TAC:CH! XST:X! INC#:ID! LT/LNG:GPS/d! UNIT(S):UNIT! TIME:TIME! CMT:INFO! " +
                    ") " +
          "| UNITS:UNIT! PN:CALL! ADD:ADDR! APT:APT! CITY:CITY! XST:X! TAC:CH! INC#:ID! TIME:TIME! NOTES:INFO! " +
          "| CALL! ( RA:SRC! UNIT(S):UNIT! ADD:ADDR! CITY:CITY! ( TAC:CH! XST:X! | XST:X! TAC:CH! ) INC#:ID! TIME:TIME! ( NOTES:INFO! | CMT:INFO! ) " +
                  "| UNITS:UNIT! ADD:ADDR! APT:APT! CITY:CITY! XST:X! TAC:CH! INC#:ID! NOTES:INFO! " +
                  ") " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "cadpage@harnett.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MISSING_BLNK_PTN = Pattern.compile("(?<=\\S)(?=(?:TIME|CMT|NOTES|UNIT\\(S\\)|PN):)");
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(?<=INC#)(?!:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Received:")) {
      int pt = body.indexOf("\n\n");
      if (pt < 0) return false;
      body = body.substring(pt+2).trim();
    }
    body = MISSING_BLNK_PTN.matcher(body).replaceAll(" ");
    body = MISSING_COLON_PTN.matcher(body).replaceAll(":");
    if (!super.parseMsg(body, data)) return false;
    String call = CALL_CODES.getCodeDescription(data.strCall);
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static Pattern INFO_BRK_PTN = Pattern.compile(",? *\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_BRK_PTN.split(field)) {
        part = part.trim();
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
