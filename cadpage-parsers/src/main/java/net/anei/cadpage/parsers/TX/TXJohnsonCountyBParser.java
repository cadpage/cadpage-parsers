package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXJohnsonCountyBParser extends FieldProgramParser {

  public TXJohnsonCountyBParser() {
    super("JOHNSON COUNTY", "TX", "ADDR! XStr:X! Box:BOX! Mapsco:MAP!");
  }
  
  @Override
  public String getFilter() {
    return "alerts@jcesddispatch.org";
  }

  private static Pattern TIME = Pattern.compile("JCESD\\|Time: \\d{2}-(\\d{2}:\\d{2}:\\d{2})");
  private static Pattern UNIT_CALL_BODY_UNIT_SRC = Pattern.compile(" *(.*?)- *(.*?): *(.*?) *\\[Units-(.*?)\\] *\\[Stations-+(.*?)-*\\]");
  private static Pattern SPACE_KILLER = Pattern.compile("\\s+");
  private static Pattern COMMA_TRIMMER = Pattern.compile("\\s*,\\s*");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
 
    // TIME
    Matcher tMat = TIME.matcher(subject);
    if (!tMat.matches()) return false;
    data.strTime = tMat.group(1);

    // split body into parseable fields
    Matcher mat = UNIT_CALL_BODY_UNIT_SRC.matcher(body);
    if (!mat.matches()) return false;

    // CALL
    data.strCall = mat.group(2).trim();

    // UNIT... check for repeats of first unit before saving
    String[] units = (mat.group(1) + mat.group(4)).split("-");
    data.strUnit = units[0] + " ";
    for (int i = 1; i < units.length; i++) {
      if (!units[0].equals(units[i])) data.strUnit += units[i] + " ";
    }
    data.strUnit = data.strUnit.trim();

    // SRC
    data.strSource = mat.group(5).replace("-", " ");

    // parse group 3 after removing unnecessary spaces
    Matcher skMat = SPACE_KILLER.matcher(mat.group(3).replace('`', ' '));
    return super.parseFields(COMMA_TRIMMER.split(skMat.replaceAll(" ")), data);
  }

  @Override
  public String getProgram() {
    return "TIME CALL UNIT " + super.getProgram() + " SRC";
  }
}
