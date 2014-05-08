lib_files := gost.sty defs.tex

protocol_files := pz1/pz.tex # abbr.tex intro.tex about.tex existing.tex labour.tex after.tex literature.tex algorithm.tex modeling.tex
pz.pdf: $(protocol_files) $(lib_files) myrefs.bib 
	-rm -f *.bbl
	pdflatex $<
	bibtex pz
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
	rm *.log *.aux *.out *.thm *.toc *.blg *.bbl
