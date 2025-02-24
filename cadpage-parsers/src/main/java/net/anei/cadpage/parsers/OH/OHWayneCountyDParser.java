package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWayneCountyDParser extends FieldProgramParser {

  public OHWayneCountyDParser() {
    this("WAYNE COUNTY", "OH");
  }

  protected OHWayneCountyDParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/1 CALL ADDR/S6 PLACE INFO! INFO/CS+? EMPTY/Z END " +
          "| ( CALL:CALL! | CALL! ) PLACE:PLACE? ADDR:ADDR! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO! INFO/N+ CROSS:X END )");
  }

  @Override
  public String getAliasCode() {
    return "OHWayneCountyD";
  }

  @Override
  public String getFilter() {
    return "info@sundance-sys.com,sunsrv@sundance-sys.com";
  }

  private static final Pattern FLAG_PTN = Pattern.compile("\\*{2,}([-/ A-Z0-9]+)\\*{2,} *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("From: ")) return false;
    data.strSource = subject.substring(6).trim();
    String flag = "";
    Matcher match = FLAG_PTN.matcher(body);
    if (match.lookingAt()) {
      flag = match.group(1);
      body = body.substring(match.end());
    }
    if (body.contains("ADDR:")) {
      setSelectValue("2");
      String[] flds = body.split("\n");
      if (flds.length > 3) {
        if (!parseFields(flds, data)) return false;
      } else {
        if (!super.parseMsg(body, data)) return false;
      }
    }
    else {
      setSelectValue("1");
      if (!parseFields(body.split(",", -1), data)) return false;
    }
    data.strCall = append(flag, " - ", data.strCall);

    data.strCity = new Parser(data.strCity).get('/');
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

}
