package net.anei.cadpage.parsers;

/**
 * This interface maintains the split message handling options.  There are
 * two implementations.  The basic one is returned ManagePreferences.getDefaultSplitMsgOptions()
 * that returns the normal configured options.  A customized SplitMsgOptionsCustom object can
 * be returned by Active911 parser with hard coded options on how to handle direct pages from
 * Active911
 */
public interface SplitMsgOptions {

  /**
   * @return true if direct page alerts should be subject to the same message merge
   * logic as text alerts
   */
  public boolean splitDirectPage();

  /**
   * @return Minimum number of messages expected to make up an alert
   */
  public int splitMinMsg();

  /**
   * @return true if a newline should be inserted between split messages when
   * they are merged into a final alert
   */
  public boolean splitBreakIns();

  /**
   * @return true if a blank should be inserted between split messages when
   * they are merged into a final alert
   */
  public boolean splitBlankIns();

  /**
   * @return true if sender address should be check for a match before we can
   * consider merging messages
   */
  public boolean splitChkSender();

  /**
   * @return true if leading blanks should be retained in the messages
   */
  public boolean splitKeepLeadBreak();

  /**
   * @return true if messages will be received in reverse order of how they
   * should be combined
   */
  public boolean revMsgOrder();

  /**
   * @return true if message order is not reliable, message parts could come in
   * any order
   */
  public boolean mixedMsgOrder();

  /**
   * @return true if leading terms in square or round brackets should not be parsed
   * as a subject in messages that are being appending to another message
   */
  public boolean noParseSubjectFollow();

  /**
   * @return if non-zero, the expected length of each split message fragment
   */
  public int splitBreakLength();

  /**
   * Number of implied blanks that may  be expected at the end of each split message
   * fragment
   * @return
   */
  public int splitBreakPad();

  /**
   * @return true if subject should be treated as part of the message text with a trailing colon
   */
  public boolean subjectColonField();

  /**
   * return true if newline should be inserted instead of multiple blanks in pad between messages
   * @return
   */
  public boolean insConditionalBreak();
}
