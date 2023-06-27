package net.anei.cadpage.parsers.IL;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


/**
 *  O'Fallon, IL
 */
public class ILOFallonParser extends DispatchA33Parser {

  public ILOFallonParser() {
    super("O'FALLON", "IL", A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "OFALLON@PUBLICSAFETYSOFTWARE.NET,CENCOM@CO.ST-CLAIR.IL.US,NOREPLYOFALLON@itiusa.com";
  }

  private static final Pattern TRIM_X_PTN = Pattern.compile("(.*?)(?<=\\b\\d{5}|[A-Z]|^)\\d\\d");

  @Override
  protected String trimCrossField(String field) {
    Matcher match = TRIM_X_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    return field;
  }
}
