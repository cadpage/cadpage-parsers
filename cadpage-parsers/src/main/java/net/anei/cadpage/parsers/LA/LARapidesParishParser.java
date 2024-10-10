package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LARapidesParishParser extends FieldProgramParser {

  public LARapidesParishParser() {
    super("RAPIDES PARISH", "LA",
          "Addr:ADDR! Cross:X! Inc_Type:CALL! REMARKS! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "mailout@kbpobox.com,cad@rapides911.org";
  }

  private static final Pattern MARKER = Pattern.compile("(\\d\\d?/\\d\\d/\\d{4}) : (\\d\\d:\\d\\d) +");
  private static final Pattern DELIM = Pattern.compile("\n:?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Alert Recieved")) return false;

    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    body = body.substring(match.end());

    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("REMARKS")) return new InfoField("Remarks\\b *(.*)");
    return super.getField(name);
  }
}
