package net.anei.cadpage.parsers.KY;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYGraysonCountyParser extends FieldProgramParser {

  public KYGraysonCountyParser() {
    super("GRAYSON COUNTY", "KY",
          "Agency:SRC! Nature:CALL! Location:ADDRCITYST! CommonName:PLACE! CrossStreet1:X! CrossStreet2:X!  Latitude:GPS1! Longitude:GPS2! " +
              "DateTime:DATETIME! Event:ID! Name:NAME? Phone:PHONE? Narrative:INFO! EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "CAD@gce911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
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
