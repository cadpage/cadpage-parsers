package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class ILStClairCountyCParser extends DispatchBCParser {

  public ILStClairCountyCParser() {
    super("ST CLAIR COUNTY", "IL", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "ELLEVILLEPD@PUBLICSAFETYSOFTWARE.NET,CENCOM@CO.ST-CLAIR.IL.US,CENCOM@CO.ST-VLAIR.IL.US,BELLEVILLEPD@OMNIGO.COM,CENCOM@OMNIGO.COM>";
  }

  private static final Pattern TRIM_X_PTN = Pattern.compile("(.*?)(?:(?<! )(?:[IV]+|D \\d)|(?<=\\b\\d{5}|[A-Z]|^)\\d\\d)");

  @Override
  protected String trimCrossField(String field) {
    Matcher match = TRIM_X_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    return field;
  }

}
