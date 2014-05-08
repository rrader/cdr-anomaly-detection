#protocol_files := protocol.tex abbr.tex intro.tex about.tex existing.tex labour.tex after.tex literature.tex algorithm.tex modeling.tex
lib_files := gost.sty defs.tex

protocol.pdf: $(lib_files) $(protocol_files) myrefs.bib 
	-rm -f protocol.bbl
	pdflatex $<
	bibtex protocol
	pdflatex $<
	pdflatex $<
	pdflatex $<

abstract.pdf: abstract/abstract.tex $(lib_files)
	pdflatex $<

appendixtitles.pdf: appendixtitles/appendixtitles.tex $(lib_files)
	pdflatex $<

tz.pdf: tz/tz.tex $(lib_files)
	pdflatex $<
	pdflatex $<

all: abstract.pdf appendixtitles.pdf protocol.pdf

clean:
	rm *.log *.aux *.out *.thm *.toc
