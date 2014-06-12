lib_files := gost.sty defs.tex

protocol_files := pz1/pz.tex pz1/about.tex pz1/abbr.tex pz1/intro.tex pz1/existing.tex labour/labour.tex pz1/after.tex pz1/literature.tex pz1/algorithm.tex pz1/modeling.tex abstract/abstract.tex
pz.pdf: $(protocol_files) $(lib_files) myrefs.bib 
	-rm -f *.bbl
	pdflatex $<
	bibtex pz
	pdflatex $<
	pdflatex $<
	pdflatex $<

abstract.pdf: abstract/abstract.tex $(lib_files)
	pdflatex $<

albumlist.pdf: albumlist.tex $(lib_files)
	pdflatex $<

appendixtitles.pdf: appendixtitles/appendixtitles.tex $(lib_files)
	pdflatex $<

tz.pdf: tz/tz.tex $(lib_files)
	pdflatex $<
	pdflatex $<

labour.pdf: labour/labour_main.tex labour/labour.tex
	-rm -f *.bbl
	pdflatex $<
	bibtex labour_main
	pdflatex $<
	pdflatex $<
	pdflatex $<

all: abstract.pdf appendixtitles.pdf protocol.pdf

clean:
	rm -f *.log *.aux *.out *.thm *.toc *.blg *.bbl
	cd pz1/; rm -f *.log *.aux *.out *.thm *.toc *.blg *.bbl
	cd labour/; rm -f *.log *.aux *.out *.thm *.toc *.blg *.bbl
	
