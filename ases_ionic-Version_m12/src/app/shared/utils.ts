export class Utils {
    public static hasNumber(value: string): boolean {
        const numeros = "0123456789"
        let temNumero: boolean = false;
        for (let i = 0; i < value.length; i++) {
            if (!temNumero) {
                temNumero = numeros.indexOf(value[i]) != -1;
            }
        }

        return temNumero;
    }

    public static hasLetter(value: string): boolean {
        const letras = "abcdefghijklmnopqrstuvwxyzáãâäàåçêëèéïîìôöòûùÿíóúüñýõ".toUpperCase()
        let temLetra: boolean = false;
        for (let i = 0; i < value.length; i++) {
            if (!temLetra) {
                temLetra = letras.indexOf(value[i]) != -1;
            }
        }

        return temLetra;
    }

    public static sanitizeString(string: string): string {
        if (!string) {
          return '';
        } else if (string === null) {
          alert('Campo descrição nulo');
        }
    
        let str = string;
    
        const hexObject = {
          a: /[\xE0-\xE6]/g,
          A: /[\xC0-\xC6]/g,
          e: /[\xE8-\xEB]/g,
          E: /[\xC8-\xCB]/g,
          i: /[\xEC-\xEF]/g,
          I: /[\xCC-\xCF]/g,
          o: /[\xF2-\xF6]/g,
          O: /[\xD2-\xD6]/g,
          u: /[\xF9-\xFC]/g,
          U: /[\xD9-\xDC]/g,
          c: /\xE7/g,
          C: /\xC7/g,
          n: /\xF1/g,
          N: /\xD1/g,
        };
    
        for (const letter in hexObject) {
          const regex = hexObject[letter];
          str = str.replace(regex, letter);
        }
    
        return str;
    }

    private static editDistance(firstString: string, secondString: string): number {
        firstString = firstString.toLowerCase();
        secondString = secondString.toLowerCase();

        const costs = [];
        for (let i = 0; i <= firstString.length; i++) {
            let lastValue = i;

            for (let j = 0; j <= secondString.length; j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        let newValue = costs[j - 1];
                        if (firstString.charAt(i - 1) != secondString.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) costs[secondString.length] = lastValue;
        }
        return costs[secondString.length];
    }

    // levenshtein
    public static similarityPercent(firstString: string, secondString: string): number {
        let longer = firstString;
        let shorter = secondString;
        if (firstString.length < secondString.length) {
            longer = secondString;
            shorter = firstString;
        }
        const longerLength = longer.length;
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - this.editDistance(longer, shorter)) / longerLength;
    }

    public static filterByApproximation(rows: Array<any>, attribute: string, keyWord?: string) {
        if (typeof keyWord === 'string') {
            return rows.filter(item => {
            if (item[attribute]) {
                const att = this.sanitizeString(item[attribute].toLowerCase());
                const keywd = this.sanitizeString(keyWord.toLowerCase());

                return (this.similarityPercent(att, keywd) >= 0.7);
            }
            return false;
            });
        }
        return rows.filter(item => item[attribute] === keyWord);
    }

    public static generateGUID(): number {
        function S4(): number {
            return Math.floor((1 + Math.random()) * 10000);
        }

        return S4() + S4();
    }
}
