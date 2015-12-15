/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bntu.fitr.povt.gapeenko.vlad.web.onlinelibrary.controllers;


import by.bntu.fitr.povt.gapeenko.vlad.web.onlinelibrary.beans.Pager;
import by.bntu.fitr.povt.gapeenko.vlad.web.onlinelibrary.db.DataHelper;
import by.bntu.fitr.povt.gapeenko.vlad.web.onlinelibrary.entity.Book;
import by.bntu.fitr.povt.gapeenko.vlad.web.onlinelibrary.enums.SearchType;
import by.bntu.fitr.povt.gapeenko.vlad.web.onlinelibrary.models.BookListDataModel;
import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Vladislav
 */
@ManagedBean(eager = true)
@SessionScoped
public class BookListController implements Serializable {

    private DataHelper dataHelper = dataHelper = DataHelper.getInstance();
    private final LazyDataModel<Book> bookListModel;
    private Long selectedAuthorId;// текущий автор книги из списка при редактировании книги
    // критерии поиска
    private char selectedLetter; // выбранная буква алфавита, по умолчанию не выбрана ни одна буква
    private SearchType selectedSearchType = SearchType.TITLE;// хранит выбранный тип поиска, по-умолчанию - по названию
    private long selectedGenreId; // выбранный жанр
    private String currentSearchString; // хранит поисковую строку
    private final Pager pager = Pager.getInstance();
    //-------
    private boolean editModeView;// отображение режима редактирования

    public BookListController() {
        bookListModel = new BookListDataModel();                       
    }

    private void submitValues(Character selectedLetter, long selectedGenreId) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
    }

    //<editor-fold defaultstate="collapsed" desc="запросы в базу">
    private void fillBooksAll() {
        dataHelper.getAllBooks();
    }

    public void fillBooksByGenre() {

        cancelEditMode();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        selectedGenreId = Long.valueOf(params.get("genre_id"));

        submitValues(' ', selectedGenreId);
        dataHelper.getBooksByGenre(selectedGenreId);


    }

    public void fillBooksByLetter() {

        cancelEditMode();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter").charAt(0);

        submitValues(selectedLetter, -1);


        dataHelper.getBooksByLetter(selectedLetter);


    }

    public void fillBooksBySearch() {

        cancelEditMode();

        submitValues(' ',  -1);

        if (currentSearchString.trim().length() == 0) {
            fillBooksAll();

        }

        if (selectedSearchType == SearchType.AUTHOR) {
            dataHelper.getBooksByAuthor(currentSearchString);
        } else if (selectedSearchType == SearchType.TITLE) {
            dataHelper.getBooksByName(currentSearchString);
        }


    }

    public void updateBooks() {

        dataHelper.update();

        cancelEditMode();

        dataHelper.populateList();

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="режим редактирования">
    public void showEdit() {
        editModeView = true;
    }

    public void cancelEditMode() {
        editModeView = false;
        for (Book book : pager.getList()) {
            book.setEdit(false);
        }
    }
    //</editor-fold>

    public Character[] getRussianLetters() {
        Character[] letters = new Character[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',};
        return letters;
    }

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        selectedSearchType = (SearchType) e.getNewValue();
    }

   
        //<editor-fold defaultstate="collapsed" desc="гетеры сетеры">
    public boolean isEditMode() {
        return editModeView;
    }

    public String getSearchString() {
        return currentSearchString;
    }

    public void setSearchString(String searchString) {
        this.currentSearchString = searchString;
    }

    public SearchType getSearchType() {
        return selectedSearchType;
    }

    public void setSearchType(SearchType searchType) {
        this.selectedSearchType = searchType;
    }

    public long getSelectedGenreId() {
        return selectedGenreId;
    }

    public void setSelectedGenreId(int selectedGenreId) {
        this.selectedGenreId = selectedGenreId;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char selectedLetter) {
        this.selectedLetter = selectedLetter;
    }

    public Long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(Long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

    public Pager getPager() {
        return pager;
    }
    
    public LazyDataModel<Book> getBookListModel() {
        return bookListModel;
    }

    

    //</editor-fold>

}
