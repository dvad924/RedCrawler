package ui;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class LimitLinesDocumentListener implements DocumentListener{

	private int lineLimit;
	
	public LimitLinesDocumentListener(int num){
		lineLimit = num;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}

	@Override
	public void insertUpdate(final DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				removeLines(e);
			}
		});
	}
	
	public void removeLines(DocumentEvent e){
		Document d = e.getDocument();
		Element root = d.getDefaultRootElement();
		while(root.getElementCount() > lineLimit){
			try{
				d.remove(0, root.getElement(0).getEndOffset());
			}catch(BadLocationException b){
			}
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		
		
	}

}
