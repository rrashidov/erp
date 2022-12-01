package org.roko.erp.services;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.repositories.CodeSerieRepository;
import org.roko.erp.services.util.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CodeSerieServiceImpl implements CodeSerieService {

    private CodeSerieRepository repo;

    public CodeSerieServiceImpl(CodeSerieRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(CodeSerie codeSerie) {
        repo.save(codeSerie);
    }

    @Override
    public void update(String code, CodeSerie codeSerie) {
        CodeSerie codeSerieFromDB = repo.findById(code).get();

        transferFields(codeSerie, codeSerieFromDB);

        repo.save(codeSerieFromDB);
    }

    @Override
    public void delete(String code) {
        CodeSerie codeSerie = repo.findById(code).get();

        repo.delete(codeSerie);
    }

    @Override
    public CodeSerie get(String code) {
        Optional<CodeSerie> codeSerieOptional = repo.findById(code);

        if (codeSerieOptional.isPresent()) {
            return codeSerieOptional.get();
        }

        return null;
    }

    @Override
    public List<CodeSerie> list() {
        return repo.findAll();
    }

    @Override
    public List<CodeSerie> list(int page) {
        return repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    @Override
    public String generate(String code) {
        CodeSerie codeSerie = repo.findById(code).get();

        String newCode = generateNewCode(codeSerie);

        codeSerie.setLastCode(newCode);

        repo.save(codeSerie);

        return newCode;
    }

    private String generateNewCode(CodeSerie codeSerie) {
        String lastCode = codeSerie.getLastCode();

        Pair<String, String> codePair = splitCode(lastCode);

        int currentNumber = strip(codePair.getT2());

        int increasedNumber = currentNumber + 1;

        String increasedNumberString = pad(codePair.getT2(), increasedNumber);

        return codePair.getT1() + increasedNumberString;
    }

    private String pad(String originalNumberString, int increasedNumber) {
        String increasedNumberString = Integer.toString(increasedNumber);

        if (increasedNumberString.length() == originalNumberString.length()) {
            return increasedNumberString;
        }

        int symbolsToPad = originalNumberString.length() - increasedNumberString.length();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < symbolsToPad; i++) {
            sb.append("0");
        }
        sb.append(increasedNumberString);

        return sb.toString();
    }

    private int strip(String numberString) {
        OptionalInt firstIndexOfNonZeroNum = numberString.chars()
            .filter(c -> c != '0')
            .map(c -> numberString.indexOf(c))
            .findFirst();

        if (!firstIndexOfNonZeroNum.isPresent()) {
            return 0;
        }

        return Integer.parseInt(numberString.substring(firstIndexOfNonZeroNum.getAsInt()));
    }

    private void transferFields(CodeSerie source, CodeSerie target) {
        target.setName(source.getName());
        target.setFirstCode(source.getFirstCode());
        target.setLastCode(source.getLastCode());
    }

    private Pair<String, String> splitCode(String code) {
        OptionalInt firstIndexOfNumberChar = code.chars()
            .filter(x -> Character.isDigit(x))
            .map(x -> code.indexOf(x))
            .findFirst();

        String codeString = code.substring(0, firstIndexOfNumberChar.getAsInt());
        String numString = code.substring(firstIndexOfNumberChar.getAsInt());

        return new Pair<String, String>(codeString, numString);
    }

}
