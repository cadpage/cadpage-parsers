package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;



public class OHHudsonParser extends FieldProgramParser {

  public OHHudsonParser() {
    super("HUDSON", "OH",
          "CALL CALL:CALL/SDS PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID UNIT:UNIT INFO:INFO XSTREET:X WS:MAP END");
  }

  private static final Pattern DELIM = Pattern.compile("(?!\n|$)(?=CALL:)|\n");

  @Override
  public String getFilter() {
    return "sunsrv@sundance-sys.com,info@sundance-sys.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From: BHDispMap")) return false;
    if (! parseFields(DELIM.split(body), data)) return false;
    return !data.strCall.isEmpty();
  }
}
