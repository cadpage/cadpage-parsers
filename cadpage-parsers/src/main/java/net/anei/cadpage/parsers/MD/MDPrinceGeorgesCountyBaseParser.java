package net.anei.cadpage.parsers.MD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

/**
 * Prince Georges County base parser class
 */
abstract class MDPrinceGeorgesCountyBaseParser extends FieldProgramParser {
  
  public MDPrinceGeorgesCountyBaseParser(String program) {
    super("PRINCE GEORGES COUNTY", "MD", program);
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    // Undo various abbreviations of CAPITAL BELTWAY
    return CAP_BELT_PTN.matcher(sAddress).replaceAll("CAPITAL BELTWAY");
  }
  private static final Pattern CAP_BELT_PTN = 
      Pattern.compile("\\bCAP BELT(?:WAY)?(?: [IO]L [A-Z]{1,2})(?: HWY)?\\b", Pattern.CASE_INSENSITIVE);
}
