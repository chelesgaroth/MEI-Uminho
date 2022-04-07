

const sportIcons = new Map()

sportIcons.set(0, '⚽' )
sportIcons.set(1, '🏀' )
sportIcons.set(2, '🏒' )
sportIcons.set(3, '🏓' )

class SportIcon {

    

    static getSportIcon(sportID) {
        return sportIcons.get(sportID);
    }
}

export default SportIcon;