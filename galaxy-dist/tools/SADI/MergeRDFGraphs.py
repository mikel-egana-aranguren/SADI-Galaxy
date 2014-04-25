
import sys
from rdflib.graph import Graph

def main(argv):
    store = Graph()
    for inputFileName in argv:
        try:
            store.parse(inputFileName)
        except Exception:
            pass
    print store.serialize()

if __name__ == "__main__":
    main(sys.argv[1:])
