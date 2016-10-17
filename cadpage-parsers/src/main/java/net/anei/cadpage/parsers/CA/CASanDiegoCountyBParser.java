package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CASanDiegoCountyBParser extends FieldProgramParser {

  public CASanDiegoCountyBParser() {
    super("SAN DIEGO COUNTY", "CA", "CALL! ADDR! APT! CH! UNIT! MAP! X! SRC! ID!");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static Pattern MASTER = Pattern.compile("NAT:(.*?) */ADR:(.*?) */APT:(.*?) */TAC:\\.?(.*?) */UNITS:(.*?) */MAP:(.*?) */XST:(.*) */(.*?)/(\\d{4}-\\d{6})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD MESSAGE")) return false;

    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;

    String[] fields = new String[] { mat.group(1), mat.group(2), mat.group(3), mat.group(4), mat.group(5), mat.group(6), mat.group(7), mat.group(8), mat.group(9) };

    return parseFields(fields, data);
  }

  private static Pattern TERRACE = Pattern.compile("\\bTR\\b", Pattern.CASE_INSENSITIVE);
  private static Pattern TRAIL = Pattern.compile("\\bTL\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String field, boolean X) {
    field = TERRACE.matcher(field).replaceAll("Terrace");
    field = TRAIL.matcher(field).replaceAll("Trail");
    
    return field;
  }

}
