package net.anei.cadpage.parsers;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * This class represents a collection of aliased parsers
 * that have been selected by the user as multiple location parsers.
 * Since they are all equivalent as far as parsing goes, we just
 * use the first one for all parsing functions.  The only thing
 * that gets adjusted as more parsers are added is the default
 * city and state and the sender filter
 */
public class AliasedMsgParser extends GroupBaseParser {
  
  private MsgParser parser;
  
  /**
   * Main constructor
   * @param parser initial parser
   */
  public AliasedMsgParser(MsgParser parser) {
    this.parser = parser;
    addParser(parser);
  }
  
  /**
   * Add additional message parser.  New parser must share
   * an alias code with the current parser
   * @param parser parser to be added
   */
  public void addMsgParser(MsgParser parser) {
    
    // Make sure they share the same alias code
    if (!parser.getAliasCode().equals(this.parser.getAliasCode())) {
      throw new RuntimeException("Parsers " + parser.getParserCode() + " and " + this.parser.getParserCode() + 
          " do not share the same alias code");
    }
    addParser(parser);
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return parser.getActive911SplitMsgOptions();
  }

  @Override
  protected Data parseMsg(Message msg, int parseFlags) {
    Data data = parser.parseMsg(msg, parseFlags, getFilter());
    if (data != null) {
      data.defCity = getDefaultCity();
      data.defState = getDefaultState();
    }
    return data;
  }

  @Override
  public String getParserCode() {
    return parser.getParserCode();
  }

  @Override
  public String getAliasCode() {
    return parser.getAliasCode();
  }
}
