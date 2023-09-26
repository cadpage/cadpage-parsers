package net.anei.cadpage.parsers.dispatch;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA93Parser extends FieldProgramParser {

  public DispatchA93Parser(String defCity, String defState) {
    super(defCity, defState,
          "Agency:SRC! Nature:CALL! Location:ADDRCITYST! CommonName:PLACE! CrossStreet1:X! CrossStreet2:X!  Latitude:GPS1! Longitude:GPS2! " +
              "DateTime:DATETIME! ( Event:ID! | Report:ID! ) Name:NAME? Phone:PHONE? Narrative:INFO! EMPTY! END");
  }

  private static final Pattern DELIM = Pattern.compile(" +\\| *\n");
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.endsWith(" |")) body +='\n';
    return parseFields(DELIM.split(body, -1), data);
  }

  private static final SimpleDateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d [AP]M", DATE_TIME_FMT, true);
    return super.getField(name);
  }
}
