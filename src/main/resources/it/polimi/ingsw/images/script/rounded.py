from PIL import Image, ImageDraw

def arrotonda_angoli_immagine(input_path, output_path, radius):
    """
    Applica bordi arrotondati a un'immagine.

    Args:
    - input_path: Percorso dell'immagine originale.
    - output_path: Percorso dell'immagine di output con angoli arrotondati.
    - radius: Raggio degli angoli arrotondati.
    """
    with Image.open(input_path).convert("RGBA") as img:
        # Crea un'immagine di maschera bianca con lo stesso size dell'immagine originale
        mask = Image.new('L', img.size, 0)
        draw = ImageDraw.Draw(mask)
        
        # Disegna quattro cerchi agli angoli e rettangoli ai bordi per creare l'effetto arrotondato
        draw.rounded_rectangle((0, 0, img.size[0], img.size[1]), radius=radius, fill=255)
        
        # Applica la maschera all'immagine originale
        img.putalpha(mask)
        
        # Salva l'immagine di output
        img.save(output_path, format="PNG")
        print(f"Immagine salvata con angoli arrotondati: {output_path}")

# Esempio di uso
input_path = 'prova_ritagliata.png'
output_path = 'rit_rounded.png'
radius = 20  # Raggio degli angoli arrotondati, adattalo secondo le tue necessità

arrotonda_angoli_immagine(input_path, output_path, radius)